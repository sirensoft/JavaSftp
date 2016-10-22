/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Ini;

/**
 *
 * @author utehn
 */
public class JavaSftp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Ini ini = null;
        try {
            ini = new Ini(new File("C:/hdc.ini"));
        } catch (IOException ex) {
            Logger.getLogger(JavaSftp.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(ini.get("config", "host"));
        System.out.println(ini.get("config", "user"));
        String host = ini.get("config", "host");
        String user = ini.get("config", "user");

        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(user,host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("qazwsxedcr112233");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            //sftpChannel.get("remotefile.txt", "localfile.txt");

            File folder = new File("../");
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                
                if (file.isFile() && file.getName().substring(file.getName().length()-3).toLowerCase().equals("zip") ) {
                    
                    sftpChannel.put("../" + file.getName(), "/var/www/html/hdc/warehouse/f43import/frontend/web/fortythree");
                    System.out.println(file.getName());
                    sftpChannel.put("../" + file.getName(), "/usr/local/apache-tomcat-8.0.21/webapps/hdc/WEB-INF/fortythree_backup");

                    //Files.move("../"+file.getName(), "/ok");
                    
                    file.renameTo(new File("../ok/" + file.getName()));
                }
            }

            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SftpException e) {
            e.printStackTrace();
        }

    }

}
