package com.example.kiebot.service;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.WinDef.HWND;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

//이부분은 그냥 win32 공부를 해라..
public interface User32 extends StdCallLibrary {

    User32 INSTANCE = Native.load("user32", User32.class);
    HWND FindWindowA(String lpClassName, String lpWindowName);
    HWND FindWindowExA(HWND hWndParent, HWND hWndChildAfter, String lpClassName, String lpWindowName);
    int SendMessageA(HWND hWnd, int Msg, int wParam, byte[] lParam);
    int PostMessageA(HWND hWnd, int Msg, int wParam, int lParam);
    int WM_SETTEXT = 0x000C;
    int WM_KEYDOWN = 0x0100;
    int WM_GETTEXT = 0x000D;

    static void sendMessage(String className, ArrayList<String> windowName, String message) throws Exception {
        try {

            for (String windowNames : windowName) {
                HWND hwnd_main = User32.INSTANCE.FindWindowA(null, windowNames);
                HWND hWnd = User32.INSTANCE.FindWindowExA(hwnd_main, null, className, null);

                if (hWnd != null) {
                    byte[] msg = Native.toByteArray(message, Charset.defaultCharset());
                    User32.INSTANCE.SendMessageA(hWnd, User32.WM_SETTEXT, 0, msg);
                    // 엔터 키 이벤트 추가
                    User32.INSTANCE.PostMessageA(hWnd, User32.WM_KEYDOWN, 0x0D, 0);
                    System.out.println("오류 메시지 전송 완료");
                } else {
                    throw new Exception(windowNames);
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage() + "단톡방이 안열려있습니다. 프로그램을 종료합니다: ");
            ArrayList<String> errors = new ArrayList<>(Arrays.asList("KIE 실험실")); //에러 메세지 보낼 카톡방 설정
            sendMessage("RICHEDIT50W",errors,"에러가 발생했음");
            Thread.sleep(5000);
            System.exit(1);
        }

}




    }
