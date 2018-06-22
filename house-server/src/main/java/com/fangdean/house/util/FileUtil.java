package com.fangdean.house.util;

import com.google.common.io.Files;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class FileUtil {

    public static File saveToLocal(MultipartFile file, String filePath) throws IOException {
        File newfile = new File(filePath + "/" + Instant.now().getEpochSecond() + "/" + file.getOriginalFilename());
        if (!newfile.exists()) {
            newfile.getParentFile().mkdirs();
            newfile.createNewFile();
        }
        Files.write(file.getBytes(), newfile);
        return newfile;
    }

}
