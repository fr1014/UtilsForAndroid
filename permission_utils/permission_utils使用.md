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
        
