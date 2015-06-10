package com.azalea.www.piccap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Panoo on 2015/6/10.
 */
public class FileUtil {

    public static List<File> getDerectoryFiles(String path){
        File directory = new File(path);
        if(directory.isDirectory()){
            return Arrays.asList(directory.listFiles());
        }else{
            return null;
        }
    }
}
