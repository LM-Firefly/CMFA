@file:UseSerializers(UUIDSerializer::class)

package com.github.kr328.clash.service.model

import android.os.Parcel
import android.os.Parcelable
import com.github.kr328.clash.service.util.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class Profile(
    val uuid: UUID,
    val name: String,
    val type: Type,
    val source: String,
    val active: Boolean,
    val interval: Long,
    val upload: Long,
    var download: Long,
    val total: Long,
    val expire: Long,
    val updatedAt: Long,
    val imported: Boolean,
    val pending: Boolean,
) : Parcelable {
    enum class Type {
        File, Url, External
    }
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(uuid.toString())
        dest.writeString(name)
        dest.writeInt(type.ordinal)
        dest.writeString(source)
        dest.writeInt(if (active) 1 else 0)
        dest.writeLong(interval)
        dest.writeLong(upload)
        dest.writeLong(download)
        dest.writeLong(total)
        dest.writeLong(expire)
        dest.writeLong(updatedAt)
        dest.writeInt(if (imported) 1 else 0)
        dest.writeInt(if (pending) 1 else 0)
    }
    companion object {
        @JvmStatic
        private fun readBoolean(parcel: Parcel): Boolean = parcel.readInt() != 0
        @JvmField
        val CREATOR: Parcelable.Creator<Profile> = object : Parcelable.Creator<Profile> {
            override fun createFromParcel(parcel: Parcel): Profile {
                val uuid = parcel.readString()?.let { UUID.fromString(it) }
                    ?: throw IllegalStateException("Null UUID in parcel")
                val name = parcel.readString()!!
                val type = Type.values()[parcel.readInt()]
                val source = parcel.readString()!!
                val active = readBoolean(parcel)
                val interval = parcel.readLong()
                val upload = parcel.readLong()
                val download = parcel.readLong()
                val total = parcel.readLong()
                val expire = parcel.readLong()
                val updatedAt = parcel.readLong()
                val imported = readBoolean(parcel)
                val pending = readBoolean(parcel)
                return Profile(
                    uuid = uuid,
                    name = name,
                    type = type,
                    source = source,
                    active = active,
                    interval = interval,
                    upload = upload,
                    download = download,
                    total = total,
                    expire = expire,
                    updatedAt = updatedAt,
                    imported = imported,
                    pending = pending,
                )
            }
        override fun newArray(size: Int): Array<Profile?> = arrayOfNulls(size)
        }
    }
}