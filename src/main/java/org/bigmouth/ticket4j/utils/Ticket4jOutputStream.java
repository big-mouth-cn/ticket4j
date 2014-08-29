package org.bigmouth.ticket4j.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


public class Ticket4jOutputStream {

    public static void write(Object object, File file) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
        }
        finally {
            IOUtils.closeQuietly(oos);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T read(File file, boolean forceDelete) throws IOException, ClassNotFoundException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        
        if (!file.exists())
            return null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            return (T) ois.readObject();
        }
        catch (ClassCastException e) {
            if (forceDelete) {
                IOUtils.closeQuietly(ois);
                try {
                    FileUtils.forceDelete(file);
                }
                catch (IOException e1) {
                }
            }
        }
        finally {
            IOUtils.closeQuietly(ois);
        }
        return null;
    }
}
