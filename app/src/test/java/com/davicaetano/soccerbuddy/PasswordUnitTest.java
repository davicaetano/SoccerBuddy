package com.davicaetano.soccerbuddy;


import android.content.Context;

import com.davicaetano.soccerbuddy.utils.Utils;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;


/**
 * Created by davicaetano on 2/13/16.
 */
public class PasswordUnitTest{

    Context context = Mockito.mock(Context.class);

    @Test
    public void Password(){
        String password = Utils.password("davicaetano@gmail.com","passwordtesteparameuprograma","salparaomeuprograma");
        String passwordExpected = "e88c8886397f56ecd42bfca0e414d4d2ddde4ecf";
        assertEquals("The password HMAC is not working as expected.",passwordExpected,password);
    }
}
