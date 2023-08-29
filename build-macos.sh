#!/usr/bin/env bash
rm -rf build
mkdir build
cd build
cmake ../java -DCMAKE_OSX_ARCHITECTURES="arm64;x86_64" -DCMAKE_BUILD_TYPE=Release
make -j4
cp -f libyoga.dylib ../java-jvm/src/main/resources
sha256sum libyoga.dylib | cut -d " " -f 1 > ../java-jvm/src/main/resources/libyoga.dylib.sha256