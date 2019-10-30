package com.fengsheng.fastdfs.utils;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * @ClassName: FaceUtils
 * @Description: 人脸照片的加密解密
 * @author 果文明
 * @date 2014年11月20日 下午1:48:30
 */
public class ImageUploadUtils {



	public static InputStream baseToInputStream(String base64string) {
		Decoder decoder = Base64.getDecoder();
		byte[] bytes1 = decoder.decode(base64string);
		try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes1)) {
			return stream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String outputStreamToBase64(BufferedImage tempImage) {

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			ImageIO.write(tempImage, "jpg", outputStream);
			Encoder encoder = Base64.getEncoder();
			return encoder.encodeToString(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}


	public static String shrinkImage(String imageBase64, int size) {

		try {
			String resultImageString = imageBase64;
			if (resultImageString == null) {
				return imageBase64;
			}
			long fileSize = resultImageString.length();

			while (fileSize > size * 1024) {
				double rate = size * 1024 * 0.9 / fileSize;
				System.out.println("rate:" + rate);
				InputStream inputStream =baseToInputStream(imageBase64);
				if (inputStream==null){
					return null;
				}
				BufferedImage bufferedImage = ImageIO.read(inputStream);
				AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(rate, rate), null);
				BufferedImage tempImage = ato.filter(bufferedImage, null);
				resultImageString = outputStreamToBase64(tempImage);
				if (resultImageString != null) {
					fileSize = resultImageString.length();
				}
			}
			return resultImageString;

		} catch (IOException e) {
			e.printStackTrace();
			return imageBase64;
		}
	}
}
