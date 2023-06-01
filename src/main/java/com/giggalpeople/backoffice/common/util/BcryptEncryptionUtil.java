package com.giggalpeople.backoffice.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * <h2><b>사용자 계정 비밀번호 암호화를 위한 Util Class</b></h2>
 */

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BcryptEncryptionUtil {

    /**
     * <b>사용자 계정 비밀번호 암호화 Method</b>
     * @param plainTextPassword 사용자가 입력한 평문 비밀번호
     * @return String - 해쉬 암호화된 사용자 비밀번호
     */

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * <b>Data Base에 저장된 해쉬 암호문과 사용자가 Login 시 입력한 평문 비밀번호를 Hashing하여 일치하는지 확인하기 위한 Method.</b>
     * @return Boolean - 비밀 번호 일치 여부
     */

    public static Boolean passwordAnalysis(String plainTextPassword, String encryptTextPassword) {
        return BCrypt.checkpw(plainTextPassword, encryptTextPassword);
    }
}
