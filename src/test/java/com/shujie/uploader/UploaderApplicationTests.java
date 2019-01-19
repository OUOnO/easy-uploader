package com.shujie.uploader;

import com.shujie.uploader.service.DesService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UploaderApplicationTests {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DesService desService;

	@Test
	public void testDES() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
		String hello_world = desService.encode("hello world");
		String decode = desService.decode(hello_world);
		Assert.assertEquals(decode, "hello world");
	}

}

