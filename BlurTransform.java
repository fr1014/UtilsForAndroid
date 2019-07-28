public class BlurTransform extends BitmapTransformation {

    private Context mContext;

    //模糊程度，区间是[1,25]
    private int blur_radius;

    //number表示透明度程度，区间是[0, 100]，0表示完全透明，1表示不透明
    private int number;

    //blur_scale表示缩放比例，区间是（0,1]的浮点数
    private float blur_scale;

    public BlurTransform(Context mContext, int blur_radius, float blur_scale, int number){
        this.mContext = mContext;
        if(blur_radius < 1){
            this.blur_radius = 1;
        }else if(blur_radius >25){
            this.blur_radius = 25;
        }else{
            this.blur_radius = blur_radius;
        }

        if(blur_scale < 0){
            this.blur_scale = 0;
        }else if(blur_scale >1){
            this.blur_scale = 1;
        }else{
            this.blur_scale = blur_scale;
        }

        if(number < 0){
            this.number = 0;
        }else if(number >100){
            this.number = 100;
        }else{
            this.number = number;
        }
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap source, int outWidth, int outHeight) {
        return getBlurBitmap(mContext, pool, source);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    /**
     * 得到模糊后的bitmap
     *
     * @param context
     * @param bitmap
     * @return
     */
    private Bitmap getBlurBitmap(Context context, BitmapPool pool, Bitmap bitmap) {

        if(bitmap == null){
            return null;
        }

        Bitmap inputBitmap = null;

        if(blur_scale != 1){
            int width = Math.round(bitmap.getWidth() * blur_scale);
            int height = Math.round(bitmap.getHeight() * blur_scale);
            inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }else{
            inputBitmap = bitmap;
        }

        if(number != 100){
            //像素数组
            int[] pixels = new int[inputBitmap.getWidth() * inputBitmap.getHeight()];
            //int[] pixels, int offset, int stride, int x, int y, int width, int height
            inputBitmap.getPixels(pixels, 0, inputBitmap.getWidth(), 0, 0, inputBitmap.getWidth(), inputBitmap.getHeight());

            number = number * 255 / 100;

            for(int i=0;i<pixels.length;i++){
                pixels[i] = (number << 24) | (pixels[i] & 0x00FFFFFF);
            }
            inputBitmap = Bitmap.createBitmap(pixels, inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
        }


        // 创建一张渲染后的输出图片。
        Bitmap outputBitmap = pool.get(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());

        RenderScript rs = RenderScript.create(context);
        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //native层分配内存空间
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        //设置blur的半径然后进行blur
        theIntrinsic.setRadius(blur_radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        //拷贝blur后的数据到java缓冲区中
        tmpOut.copyTo(outputBitmap);
        //销毁Renderscript
        rs.destroy();

        inputBitmap.recycle();

        return outputBitmap;
    }
}
