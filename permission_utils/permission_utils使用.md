 ### 1.在Activity或者Fragment中使用
 ```
 //Api大于26需要请求权限
 if (PermissionUtils.needRequestPermission()){
            Permission.with(this)
                    .requestCode(100)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callBack(new PermissionListener() {
                        @Override
                        public void onPermit(int requestCode, String... permission) {
                  
                            //TODO 已获取到权限，执行存储操作
                            
                        }

                        @Override
                        public void onCancel(int requestCode, String... permission) {
                            //用户拒绝给予授权，提醒是否需要进入权限设置界面
                            //确定后跳转至当前app的权限设置界面
                            PermissionUtils.goSetting(mContext);
                        }
                    })
            .send();
        }else {
            
           //TODO Api低于26不需要请求权限，直接进行存储操作
        }
 }       
 ```       
        
 ### 2.Activvity中添加回调
 
 ```
//必须添加，否则第一次请求成功权限不会走回调
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       Permission.onRequestPermissionResult(requestCode, permissions, grantResults);
}
```    
### 3.在Fragment中添加回调
- 3.1 由于Fragment中无法直接回调，所以需要先在Activity中添加如下代码
```
//Fragment中无法回调onRequestPermissionsResult
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       // 获取到Activity下的Fragment
       List<Fragment> fragments = getSupportFragmentManager().getFragments();
       if (fragments == null) {
            return;
       }
       // 查找在Fragment中onRequestPermissionsResult方法并调用
       for (Fragment fragment : fragments) {
           if (fragment != null) {
               // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
               fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
           }
       }
}
```
- 3.2 然后在Fragment中添加如下代码
```
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       Permission.onRequestPermissionResult(requestCode, permissions, grantResults);
}
```
