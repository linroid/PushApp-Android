/*
 * Copyright (c) linroid 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linroid.pushapp.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

/**
 * Created by linroid on 8/21/15.
 */
public class MD5 {

    /**
     * 检验目标文件的MD5是否正确
     * @param md5
     * @param updateFile
     * @return
     */
    public static boolean checkMD5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            return false;
        }
        String calculatedDigest = calculateFile(updateFile);
        if (calculatedDigest == null) {
            return false;
        }
        return calculatedDigest.equalsIgnoreCase(md5);
    }

    /**
     * 计算文件MD5
     * @param file 要计算的文件
     * @return 32位MD5
     */
    public static String calculateFile(File file) {
//        try {
//            Process process = Runtime.getRuntime().exec("md5 " + file.getAbsolutePath());
//            InputStream is = process.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e, "NoSuchAlgorithmException");
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Timber.e(e, "文件不存在");
            return null;
        }

        byte[] buffer = new byte[8193];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5Bytes = digest.digest();
            return convertHashToString(md5Bytes);
        } catch (IOException e) {
            Timber.e(e, "计算文件MD5异常");
        } finally {
            try {
                is.close();
            } catch (IOException ignore) {
            }
        }
        return null;
    }

    private static String convertHashToString(byte[] hashBytes) {
//
//        BigInteger bigInt = new BigInteger(1, md5sum);
//        String output = bigInt.toString(16);
//        // Fill to 32 chars
//        output = String.format("%32s", output).replace(' ', '0');
        String returnVal = "";
        for (byte hashByte : hashBytes) {
            returnVal += Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1);
        }
        return returnVal.toLowerCase();
    }
}
