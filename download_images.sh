#!/bin/bash
# Automatically downloads images from picsum.photos which uses Unsplash.com as the source.
# wget utility must be installed.

cd "img" # Folder to save images to

for i in {1..250}; do
	echo "Do it #$i"
	wget "https://picsum.photos/200/200/?random" -O "$i.jpg"
done
