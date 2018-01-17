package com.nfl.libraryoflibrary.utils;
/**
 * Created by fuli.niu on 2016/8/19.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by fuli.niu 2016/8/19
 */
public class ExecShell {
    private static String LOG_TAG = ExecShell.class.getName();

    public enum SHELL_CMD {
        check_su_binary(new String[]{"/system/xbin/which", "su"});
        String[] command;

        SHELL_CMD(String[] command) {
            this.command = command;
        }
    }

    public ArrayList<String> executeCommand(String[] commands) {
        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;
        try {
            localProcess = Runtime.getRuntime().exec(commands);
        } catch (Exception e) {
            LogTool.e("命令行执行失败");
            e.printStackTrace();
            return null;
        }
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
        try {
            while ((line = in.readLine()) != null) {
                fullResponse.add(line);
            }
        } catch (Exception e) {
            LogTool.e("命令行结果处理失败");
            e.printStackTrace();
        }
        return fullResponse;
    }

    public ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
        return executeCommand(shellCmd.command);
    }
}
