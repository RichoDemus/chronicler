#!/bin/bash
docker run -it --rm -p 8080:8080 -e "SAVE_TO_DISK=true" -e "DATA_DIR=data/" -v `pwd`/data/:/chronicler/data/ richodemus/chronicler:latest
