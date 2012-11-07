# Test: Crop a raw image into 1000 pieces
#
# Results:
#  5min+. I gave up waiting.

gm = require "gm"
async = require "async"

crop = (imageName, numCropX=100, numCropY=100, outPrefix="") ->
  img = gm imageName
  img.size (err, value) ->
    if err
      console.log err
      return

    # Create a queue object with concurrency 2
    q = async.queue (task, callback) ->
      img.crop(task.w, task.h, task.x, task.y)
        .write task.out, (err) ->
          if err
            console.log err
          do callback
    , 8

    # Assign a callback
    q.drain = ->
      console.log "all items have been processed"

    # Add crop tasks to the queue
    cropWidth = value.width / numCropX
    cropHeight = value.height / numCropY
    x = 0
    y = 0
    for i in [1..numCropY]
      for j in [1..numCropX]
        q.push
          w: cropWidth
          h: cropHeight
          x: x
          y: y
          out: "cropped/#{outPrefix}img#{i}-#{j}.bmp"

crop "image.tiff"
