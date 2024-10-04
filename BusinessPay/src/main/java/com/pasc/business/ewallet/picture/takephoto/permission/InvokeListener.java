package com.pasc.business.ewallet.picture.takephoto.permission;

import com.pasc.business.ewallet.picture.takephoto.model.InvokeParam;

/**
 * 授权管理回调
 */
public interface InvokeListener {
    PermissionManager.TPermissionType invoke(InvokeParam invokeParam);
}
