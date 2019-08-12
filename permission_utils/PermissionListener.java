/**
 * 创建时间：2019/8/11
 * 作者：范瑞
 * 博客：https://www.jianshu.com/u/408f3c1b46a9
 */
public interface PermissionListener {
    /**
     * 授权
     */
    void onPermit(int requestCode, String... permission);
    /**
     * 未授权
     */
    void onCancel(int requestCode, String... permission);
}
