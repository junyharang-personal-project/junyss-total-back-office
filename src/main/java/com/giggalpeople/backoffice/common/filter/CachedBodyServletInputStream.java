package com.giggalpeople.backoffice.common.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <h2><b>CachedBodyRequestWrapper의 getInputStream()이 ServletInputStream을 반환하기 때문에 반환값을 Handling하기 위한 Class</b></h2>
 */
@Slf4j
public class CachedBodyServletInputStream extends ServletInputStream {

    private final InputStream cachedBodyInputStream;

    /**
     * <b>Byte Array을 매개 변수로 받아 해당 Array를 이용하여 새로운 ByteArrayInputStream 객체 생성을 하고, 전역 변수 cachedBodyInputStream에게 해당 값 전달</b>
     */
    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
    }

    /**
     * <b>InputStream에 읽을 Data가 더 있는지 확인하는 Method</b>
     * @return boolean 현재 읽을 수 있는 Byte 수가 0이라면 True 0이 아니라면 false 반환
     */
    @Override
    public boolean isFinished() {
        // 현재 읽을 수 있는 Byte 수가 0이라면 True를 반환
        try {
            return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
            log.error("cachedBodyInputStream.available 실패");
        }
        return false;
    }

    /**
     * <b>InputStream이 읽을 준비가 되었는지 여부 확인</b>
     * @return boolean 기존에 InputStream을 Byte Array로 복사 했기 때문에 항상 사용할 수 있음을 나타내기 위해 true 반환
     */
    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }

    /**
     * <b>InputStream에서 한 Byte를 읽어 정수값으로 반환.</b>
     * @return int InputStream에서 한 Byte를 읽어 정수값으로 반환.
     */
    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }
}
