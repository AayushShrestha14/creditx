package com.sb.solutions.core.utils.apims;

import com.sb.solutions.core.exception.InvalidSignatureException;
import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public class SignatureUtil {

    public static String signSHA256RSA(String input, String certFilePath)
            throws InvalidSignatureException {

        final List<String> pemLines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(ResourceUtils.getURL(certFilePath).openStream()))) {
            Stream<String> lines = bufferedReader.lines();
            lines.forEach(pemLines::add);
        } catch (IOException e) {
            throw new InvalidSignatureException("Can not read cert key");
        }

        pemLines.remove(0);
        pemLines.remove(pemLines.size() - 1);
        String pem = String.join("", pemLines);
        byte[] b1 = Base64.getDecoder().decode(pem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");

            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(kf.generatePrivate(spec));
            privateSignature.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] s = privateSignature.sign();
            return Base64.getEncoder().encodeToString(s);
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidSignatureException(e.getLocalizedMessage());
        } catch (InvalidKeySpecException e) {
            throw new InvalidSignatureException(e.getLocalizedMessage());
        } catch (java.security.SignatureException e) {
            throw new InvalidSignatureException(e.getLocalizedMessage());
        } catch (InvalidKeyException e) {
            throw new InvalidSignatureException(e.getLocalizedMessage());
        }
    }
}
