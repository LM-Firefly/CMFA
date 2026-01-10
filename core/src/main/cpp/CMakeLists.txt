cmake_minimum_required(VERSION 3.0)

# 获取 git 信息（健壮处理：无 git 或不在仓库时给出回退值）
message(STATUS "CMAKE_CURRENT_SOURCE_DIR= ${CMAKE_CURRENT_SOURCE_DIR}")

# 解析 mihomo 子模块路径，优先使用该仓库的 git 信息
get_filename_component(_parent_dir "${CMAKE_CURRENT_SOURCE_DIR}" DIRECTORY)      # core/src/main
get_filename_component(_src_root "${_parent_dir}" DIRECTORY)                     # core/src
set(MIHOMO_SUBMODULE_DIR "${_src_root}/foss/golang/clash")

if (NOT EXISTS "${MIHOMO_SUBMODULE_DIR}")
  message(WARNING "mihomo submodule directory not found at ${MIHOMO_SUBMODULE_DIR}, fallback to current source dir")
  set(MIHOMO_SUBMODULE_DIR "${CMAKE_CURRENT_SOURCE_DIR}")
endif ()

# 查找 git 可执行文件
find_program(GIT_EXECUTABLE git)

set(COMMIT_HASH "")
set(CURRENT_BRANCH "")

if (GIT_EXECUTABLE)
  # 获取短哈希（7位），失败则留空以便后续回退
  execute_process(
    COMMAND ${GIT_EXECUTABLE} rev-parse --short=7 HEAD
    WORKING_DIRECTORY ${MIHOMO_SUBMODULE_DIR}
    OUTPUT_VARIABLE COMMIT_HASH
    OUTPUT_STRIP_TRAILING_WHITESPACE
    ERROR_QUIET
  )

  # 获取分支名；若处于 detached HEAD 则使用 FLAVOR_NAME
  execute_process(
    COMMAND ${GIT_EXECUTABLE} rev-parse --abbrev-ref HEAD
    WORKING_DIRECTORY ${MIHOMO_SUBMODULE_DIR}
    OUTPUT_VARIABLE CURRENT_BRANCH
    OUTPUT_STRIP_TRAILING_WHITESPACE
    ERROR_QUIET
  )

  # 如果处于 detached HEAD 状态，使用 FLAVOR_NAME（Alpha/Meta）作为分支名
  if ("${CURRENT_BRANCH}" STREQUAL "HEAD" OR "${CURRENT_BRANCH}" STREQUAL "")
    if (DEFINED FLAVOR_NAME AND NOT "${FLAVOR_NAME}" STREQUAL "")
      set(CURRENT_BRANCH "${FLAVOR_NAME}")
      message(STATUS "Using FLAVOR_NAME as branch: ${CURRENT_BRANCH}")
    else ()
      execute_process(
        COMMAND ${GIT_EXECUTABLE} describe --tags --always --dirty
        WORKING_DIRECTORY ${MIHOMO_SUBMODULE_DIR}
        OUTPUT_VARIABLE CURRENT_BRANCH
        OUTPUT_STRIP_TRAILING_WHITESPACE
        ERROR_QUIET
      )
    endif ()
  endif ()
endif ()

# 回退默认值，避免空字符串导致后续 string()/list() 报错
if (NOT COMMIT_HASH OR COMMIT_HASH STREQUAL "")
  set(COMMIT_HASH "0000000")
endif ()
if (NOT CURRENT_BRANCH OR CURRENT_BRANCH STREQUAL "")
  set(CURRENT_BRANCH "unknown")
endif ()

# 清洗分支名：去掉 Prerelease- 前缀及 -dirty 等尾缀
string(REGEX REPLACE "^Prerelease-" "" CURRENT_BRANCH "${CURRENT_BRANCH}")
string(REGEX REPLACE "-dirty$" "" CURRENT_BRANCH "${CURRENT_BRANCH}")

message(STATUS "git hash= ${COMMIT_HASH}")
message(STATUS "git current branch = ${CURRENT_BRANCH}")

# 获取生成时间
string(TIMESTAMP COMPILE_TIME "%y%m%d")
string (REGEX REPLACE "[\n\t\r]" "" COMPILE_TIME "${COMPILE_TIME}")
string(REGEX REPLACE "\"" "" COMPILE_TIME "${COMPILE_TIME}")

# 生成版本信息：统一格式为 ${CURRENT_BRANCH}_${COMMIT_HASH}_${COMPILE_TIME}
set(GIT_VERSION "${CURRENT_BRANCH}_${COMMIT_HASH}_${COMPILE_TIME}")
message(STATUS "version info = ${GIT_VERSION}")

# 去除空格
string(REGEX REPLACE "[ ]+" "" GIT_VERSION "${GIT_VERSION}")

# 保存变量到文件
configure_file(${CMAKE_CURRENT_SOURCE_DIR}/version.h.in ${CMAKE_CURRENT_SOURCE_DIR}/version.h @ONLY)

project(clash-bridge C)


set(CMAKE_POSITION_INDEPENDENT_CODE on)
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -O3")

set(GO_OUTPUT_BASE ${GO_OUTPUT}/${FLAVOR_NAME})

if ("${CMAKE_BUILD_TYPE}" STREQUAL "Debug")
    set(GO_OUTPUT_BASE "${GO_OUTPUT_BASE}Debug")
elseif ("${CMAKE_BUILD_TYPE}" STREQUAL "Release")
    set(GO_OUTPUT_BASE "${GO_OUTPUT_BASE}Release")
elseif ("${CMAKE_BUILD_TYPE}" STREQUAL "RelWithDebInfo")
    set(GO_OUTPUT_BASE "${GO_OUTPUT_BASE}Release")
else ()
    message(FATAL_ERROR "Unknown build type ${CMAKE_BUILD_TYPE}")
endif ()

include_directories("${GO_OUTPUT_BASE}/${CMAKE_ANDROID_ARCH_ABI}")
include_directories("${GO_SOURCE}")

link_directories("${GO_OUTPUT_BASE}/${CMAKE_ANDROID_ARCH_ABI}")

add_library(bridge SHARED main.c jni_helper.c bridge_helper.c)
target_link_libraries(bridge log clash)
