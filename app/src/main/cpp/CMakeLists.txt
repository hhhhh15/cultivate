cmake_minimum_required(VERSION 3.10)
project(Live2dDemo)

set(APP_NAME Live2dDemo)
# Set directory paths.
set(SDK_ROOT_PATH ${CMAKE_CURRENT_SOURCE_DIR}/Live2D)
set(CORE_PATH ${SDK_ROOT_PATH}/Core)
set(FRAMEWORK_PATH ${SDK_ROOT_PATH}/Framework)
set(COMMON_SRC_PATH ${SDK_ROOT_PATH}/Samples/Common)
set(STB_PATH ${SDK_ROOT_PATH}/stb)

# Specify version of compiler.
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)


# 添加Cubism核心库.
# 导入静态库
add_library(Live2DCubismCore STATIC IMPORTED)
# 添加库属性，找到该库，带上该库要用的头文件
set_target_properties(Live2DCubismCore
        PROPERTIES
        IMPORTED_LOCATION
        ${CORE_PATH}/lib/android/${ANDROID_ABI}/libLive2DCubismCore.a
        INTERFACE_INCLUDE_DIRECTORIES ${CORE_PATH}/include
        )


#设置变量名FRAMEWORK_SOURCE的值为OpenGL，子目录也能调用到这个变量
set(FRAMEWORK_SOURCE OpenGL)
# 编译子文件Framework下的CMakeList生成库，将生成的库放到一个新文件下，CMake自动创建
add_subdirectory(${FRAMEWORK_PATH} ${CMAKE_CURRENT_BINARY_DIR}/out/Framework)
# Framework库中编译宏定义代码块
target_compile_definitions(Framework PUBLIC CSM_TARGET_ANDROID_ES2)


add_library(${APP_NAME} SHARED)
#生成这个common库，不知道有啥用
add_subdirectory(${COMMON_SRC_PATH} ${CMAKE_CURRENT_BINARY_DIR}/out/src/Common)
#添加cpp/下写的native模块
add_subdirectory(${SDK_ROOT_PATH}/kkk)
# 将生成的库链接到App里面
target_link_libraries(${APP_NAME}
        Framework
        Live2DCubismCore
        GLESv2
        log
        )
# Specify include directories.
target_include_directories(${APP_NAME} PRIVATE ${STB_PATH})


