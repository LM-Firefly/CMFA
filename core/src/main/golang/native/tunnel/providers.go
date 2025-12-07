package tunnel

import (
	"errors"
	"encoding/json"
	"fmt"
	"time"

	"github.com/metacubex/mihomo/constant/provider"
	"github.com/metacubex/mihomo/log"
	"github.com/metacubex/mihomo/tunnel"
)

var ErrInvalidType = errors.New("invalid type")

type Provider struct {
	Name        string `json:"name"`
	VehicleType string `json:"vehicleType"`
	Type        string `json:"type"`
	UpdatedAt   int64  `json:"updatedAt"`
	SubscriptionInfo *SubscriptionInfo `json:"subscriptionInfo,omitempty"`
}

type SubscriptionInfo struct {
	Upload int64 `json:"Upload"`
	Download int64 `json:"Download"`
	Total int64 `json:"Total"`
	Expire int64 `json:"Expire"`
}

type UpdatableProvider interface {
	UpdatedAt() time.Time
}

func QueryProviders() []*Provider {
	r := tunnel.RuleProviders()
	p := tunnel.Providers()

	providers := make([]provider.Provider, 0, len(r)+len(p))

	for _, rule := range r {
		if rule.VehicleType() == provider.Compatible {
			continue
		}

		providers = append(providers, rule)
	}

	for _, proxy := range p {
		if proxy.VehicleType() == provider.Compatible {
			continue
		}

		providers = append(providers, proxy)
	}

	result := make([]*Provider, 0, len(providers))

	for _, p := range providers {
		updatedAt := time.Time{}

		if s, ok := p.(UpdatableProvider); ok {
			updatedAt = s.UpdatedAt()
		}

		// Attempt to marshal the provider itself (so we can reuse any provider's custom MarshalJSON implementation)
		// and unmarshal into our simple Provider struct to include subscriptionInfo when present.
		if raw, err := json.Marshal(p); err == nil {
			// Unmarshal into a map to handle UpdatedAt either as number (ms) or RFC3339 string
			var data map[string]json.RawMessage
			if err := json.Unmarshal(raw, &data); err == nil {
				var item Provider
				if v, ok := data["updatedAt"]; ok {
					// try number
					var ms int64
					if err := json.Unmarshal(v, &ms); err == nil {
						item.UpdatedAt = ms
						// remove to let json.Unmarshal fill rest
						delete(data, "updatedAt")
					} else {
						// try string time
						var s string
						if err := json.Unmarshal(v, &s); err == nil {
							if t, err := time.Parse(time.RFC3339, s); err == nil {
								item.UpdatedAt = t.UnixNano()/1000/1000
							}
							delete(data, "updatedAt")
						}
					}
				}

				// marshal back without updatedAt to fill remaining fields
				if rest, err := json.Marshal(data); err == nil {
					if err := json.Unmarshal(rest, &item); err == nil {
						result = append(result, &item)
						continue
					}
				}
			}
		}

		// fallback to manual population
		result = append(result, &Provider{
			Name:        p.Name(),
			VehicleType: p.VehicleType().String(),
			Type:        p.Type().String(),
			UpdatedAt:   updatedAt.UnixNano() / 1000 / 1000,
		})
	}

	return result
}

func UpdateProvider(t string, name string) error {
	err := ErrInvalidType

	switch t {
	case "Rule":
		p := tunnel.RuleProviders()[name]
		if p == nil {
			return fmt.Errorf("%s not found", name)
		}

		err = p.Update()
	case "Proxy":
		p := tunnel.Providers()[name]
		if p == nil {
			return fmt.Errorf("%s not found", name)
		}

		err = p.Update()
	}

	if err != nil {
		log.Warnln("Updating provider %s: %s", name, err.Error())
	}

	return err
}
