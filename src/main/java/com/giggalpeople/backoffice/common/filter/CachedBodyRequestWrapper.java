package com.giggalpeople.backoffice.common.filter;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * <h2><b>CachedBody Body값을 저장하고 읽을 수 있게 하는 Class</b></h2>
 */
public class CachedBodyRequestWrapper extends HttpServletRequestWrapper {

    /**
     * <b>Http Request 정보를 파싱하고, 가공하여 담을 수 있는 Arrary 변수</b>
     */
    private final byte[] cachedBody;

    /**
     * <b>해당 생성자를 통해 HttpServletRequest안에 있는 Request Body를 cachedBody에 캐싱</b>
     * @param request HTTP Request 정보를 담은 객체
     * @throws IOException 입, 출력 문제 발생 관련 Exception
     */

    public CachedBodyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    /**
     * <b>ServletInputStream을 반환하는 Method로 해당 추상 Class를 구현한 Class를 반환</b>
     * @return ServletInputStream - Http Request 정보를 파싱하고, 가공하여 담을 수 있는 Arrary
     */

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(this.cachedBody);
    }

    /**
     * <b>Caching한 Body로 부터 새로운 Stream을 생성하여 반환</b>
     * @return BufferedReader - 버퍼(Data를 다른 곳으로 전송하는 동안 일시적으로 임시 Memory 보관하는 영역)를 이용하여 읽은 값을 반환
     */

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    /**
     * <b>HTTP Request Body의 파싱하여 저장한 정보를 반환하는 Method</b>
     * @return String 파싱하여 저장한 Body 정보
     */
    public String getBody() {
        return new String(cachedBody);
    }
}
