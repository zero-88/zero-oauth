package com.zero.oauth.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.nio.file.InvalidPathException;

import org.junit.Assert;
import org.junit.Test;

import com.zero.oauth.core.TestBase;
import com.zero.oauth.core.exceptions.OAuthException;

public class FileUtilsTest extends TestBase {

    private static final URL RESOURCE = FileUtilsTest.class.getClassLoader().getResource("none_private_key.txt");

    @Test(expected = FileSystemException.class)
    public void test_readFileToString_FileURINotFound() throws Throwable {
        readErrorFile("file://tmp/xx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_readFileToString_URISyntaxError() throws Throwable {
        readErrorFile(":xxx//tmp<>.|]\u0000/test2.txt");
    }

    @Test(expected = IOException.class)
    public void test_readFileToString_FileNotFound() throws Throwable {
        readErrorFile("/tmp/xx");
    }

    @Test
    public void test_readFile_Success() {
        Assert.assertEquals("hello", FileUtils.readFileToString(RESOURCE.toString()));
    }

    @Test
    public void test_getFileName() {
        Assert.assertEquals("none_private_key.txt", FileUtils.getFileName(RESOURCE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_toPath_Blank() {
        FileUtils.toPath("");
    }

    @Test
    public void test_toPath_Not_File() {
        try {
            System.out.println(FileUtils.toPath("https://postman-echo.com/post"));
        } catch (OAuthException e) {
            Assert.assertTrue(!isWin() || e.getCause() instanceof InvalidPathException);
        }
    }

    @Test
    public void test_toPath() {
        System.out.println(FileUtils.toPath(RESOURCE.toString()));
    }

    @Test(expected = FileNotFoundException.class)
    public void test_toStream_From_Url_Not_File() throws Throwable {
        try {
            FileUtils.toStream(new URL("https://postman-echo.com/post"));
        } catch (OAuthException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NullPointerException.class)
    public void test_toStream_From_Null_Url() {
        URL url = null;
        Assert.assertNotNull(FileUtils.toStream(url));
    }

    @Test
    public void test_toStream_From_Url() {
        Assert.assertNotNull(FileUtils.toStream(RESOURCE));
    }

    @Test(expected = FileNotFoundException.class)
    public void test_toStream_From_File_Not_Found() throws Throwable {
        try {
            FileUtils.toStream(new File("/tmp/xx"));
        } catch (OAuthException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NullPointerException.class)
    public void test_convertToBytes_NullStream() {
        Assert.assertNotNull(FileUtils.convertToBytes(null));
    }

    @Test
    public void test_convertToBytes() {
        Assert.assertNotNull(
            FileUtils.convertToBytes(FileUtilsTest.class.getClassLoader().getResourceAsStream("none_private_key.txt")));
    }

    @Test(expected = NullPointerException.class)
    public void test_writeToOutputStream_Null_Input() {
        FileUtils.writeToOutputStream(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void test_writeToOutputStream_Null_Output() {
        FileUtils.writeToOutputStream(FileUtilsTest.class.getClassLoader().getResourceAsStream("none_private_key.txt"),
                                      null);
    }

    @Test
    public void test_writeToOutputStream() {
        Assert.assertNotNull(FileUtils.writeToOutputStream(
            FileUtilsTest.class.getClassLoader().getResourceAsStream("none_private_key.txt"),
            new ByteArrayOutputStream()));
    }

    @Test
    public void test_convertToByteArray() {
        Assert.assertNotNull(FileUtils.convertToByteArray(
            FileUtilsTest.class.getClassLoader().getResourceAsStream("none_private_key.txt")));
    }

    private void readErrorFile(String filePath) throws Throwable {
        try {
            FileUtils.readFileToString(filePath);
        } catch (OAuthException ex) {
            throw ex.getCause();
        }
    }

}
