#!/usr/bin/env python

# Test: Crop a raw image into 1000 pieces 24 times
#
# Results: 
#  Function=multithreaded_crop_test, Time=8.36515402794
#  Function=multiprocessed_crop_test, Time=5.3536491394

import os
from time import time
from PIL import Image
from multiprocessing import Pool
from multiprocessing.pool import ThreadPool

def st_time(func):
    def st_func(*args, **kwargs):
        t1 = time()
        r = func(*args, **kwargs)
        t2 = time()
        print "Function=%s, Time=%s" % (func.__name__, t2 - t1)
        return r
    return st_func

# only create 1,000 images, not 10,000
def crop(image_name, num_crop_x=50, num_crop_y=20, out_prefix=""):
    im = Image.open(image_name)
    width, height = im.size
    crop_width = width / num_crop_x
    crop_height = height / num_crop_y
    left_x = top_y = 0
    right_x = crop_width
    bottom_y = crop_height

    for i in xrange(1, num_crop_y):
        # split files up into dirs, so the FS doesn't hate us so much
        dirname = "cropped/%s-%s" % (out_prefix, i)
        imgs = []
        if not os.path.exists(dirname):
            os.makedirs(dirname)
        for j in xrange(1, num_crop_x):
            cropped = im.crop((left_x, top_y, right_x, bottom_y))
            imgs.append(
                    (cropped, "%s/%simg%s-%s.jpg" % (dirname,out_prefix, i, j))
                    )
            left_x += crop_width
            right_x += crop_width
        # push IO out in chunks.
        for img in imgs:
            img[0].save(img[1])
        left_x = 0
        right_x = crop_width
        top_y += crop_height
        bottom_y += crop_height

@st_time
def multithreaded_crop_test():
    p = ThreadPool()
    for i in xrange(1, 24):
        p.apply_async(crop, ("image.tiff",), {"out_prefix": "t-%s-" % i})
    p.close()
    p.join()

@st_time
def multiprocessed_crop_test():
    p = Pool()
    for i in xrange(1, 24):
        p.apply_async(crop, ("image.tiff",), {"out_prefix": "p-%s-" % i})
    p.close()
    p.join()

def main():
    multithreaded_crop_test()
    multiprocessed_crop_test()

if __name__ == "__main__":
    main()
