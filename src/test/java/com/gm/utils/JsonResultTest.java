package com.gm.utils;

import com.gm.model.response.JsonResult;
import com.gm.utils.base.Logger;
import org.junit.Test;

import java.util.ArrayList;

/**
 * The type Json utils test.
 *
 * @author Jason
 */
public class JsonResultTest {

    /**
     * com.gm.utils.Main.
     */
    @Test
    public void main(){
        Logger.info(new JsonResult(1,2,3L, new ArrayList()));
    }
}
