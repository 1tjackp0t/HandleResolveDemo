package com.ltjack;

import net.handle.hdllib.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description: 主启动
 * @author: ltjack
 * @createTime: 2020-10-18 16:05
 */
public class App {

    public static void main(String[] args) {
        System.out.println("app online!");
//        resolveGlobalHandle("20.1000/113");
        resolveLocalHandle("12345/1002");
    }

    private static void resolveGlobalHandle(String handle) {
        HandleResolver resolver = new HandleResolver();
        // 显示与服务器间请求交互的过程
        resolver.traceMessages = true;
        try {
            HandleValue[] hv1 = resolver.resolveHandle(handle);
            for (HandleValue handleValue : hv1) {
                System.out.println(handleValue);
            }
        } catch (HandleException e) {
            System.out.println(e.toString());
        }
    }

    private static void resolveLocalHandle(String handle) {
        HandleResolver resolver = new HandleResolver();
        resolver.traceMessages = true;
        try {
            InetAddress handlerServer = InetAddress.getByName("172.22.105.132");
            byte[] bytes = Util.encodeString(handle);
            byte[][] types = new byte[0][];
            int[] indexes = new int[0];
            ResolutionRequest request = new ResolutionRequest(bytes, types, indexes, null);
            AbstractResponse response = resolver.sendHdlTcpRequest(request, handlerServer, 2641);
            ResolutionResponse resolutionResponse = (ResolutionResponse) response;
            HandleValue[] handleValues = resolutionResponse.getHandleValues();
            for (HandleValue value : handleValues) {
                String type = new String(value.getType(), StandardCharsets.UTF_8);
                System.out.println(type);
                System.out.println(new String(value.getData(), StandardCharsets.UTF_8));
            }
        } catch (UnknownHostException | HandleException e) {
            e.printStackTrace();
        }
    }
}
