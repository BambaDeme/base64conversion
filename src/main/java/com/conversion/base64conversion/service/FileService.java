package com.conversion.base64conversion.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import java.io.*;
@Service
public class FileService {
    @Value("${image.size}")
    private Integer imageSize;

    public byte[] convertImage(String base64File) throws IOException {
        String base64Header = base64File.split(",")[0];
        String base64Image = base64File.split(",")[1];

        String extension = base64Header.split(";")[0].split("/")[1];

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

        String fileName = "conversion_"+UUID.randomUUID()+"."+extension;
        FileUtils.writeByteArrayToFile(new File(fileName), decodedBytes);
        return this.resizeImage(fileName);
        //return this.resizeImage(decodedBytes);

    }

    public byte[] convertPdf(String base64File) throws IOException {
        String base64Header = base64File.split(",")[0];
        String base64Image = base64File.split(",")[1];

        String extension = base64Header.split(";")[0].split("/")[1];

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        FileUtils.writeByteArrayToFile(new File("conversion"+uuid+"."+extension), decodedBytes);

        return decodedBytes;
    }

    public byte[] resizeImage(String file) throws IOException {
        Path path = Paths.get(file);
        File fileName = path.toFile();

            BufferedImage bufferedImage = ImageIO.read(fileName); // read the image
            // resize image with default defined size imageSize
            BufferedImage outputImage = Scalr.resize(bufferedImage,Scalr.Method.QUALITY ,imageSize);

            // choose a file for the compressed image baseName+imageSize
            String newFileName = FilenameUtils.getBaseName(fileName.getName())
                    + "_" + imageSize.toString() + "."
                    + FilenameUtils.getExtension(fileName.getName());

            //
            Path newPath = Paths.get(newFileName);
            //
            File newImageFile = newPath.toFile();

            ImageIO.write(outputImage, "jpg", newImageFile);
            outputImage.flush();

            return Files.readAllBytes(Paths.get(newFileName));

    }

    // resize without saving
    public byte[] resizeImage(byte[] imageBytes) throws IOException {
        InputStream is = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(is); // read the image

        BufferedImage outputImage = Scalr.resize(bufferedImage,Scalr.Method.QUALITY ,imageSize);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(outputImage, "jpg", baos);
        //outputImage.flush();

        return baos.toByteArray();

    }
}
