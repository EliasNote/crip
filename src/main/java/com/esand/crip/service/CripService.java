package com.esand.crip.service;

import com.esand.crip.entity.Crip;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

@Service
public class CripService {
    private static final String ALGORITHM = "AES";

    public String gerarChave() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(256);

            SecretKey secretKey = keyGen.generateKey();

            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar a chave de criptografia", e);
        }
    }

    public String processar(Crip crip) {
        if ("criptografia".equalsIgnoreCase(crip.getOperacao())) {
            return criptografar(crip.getSenha(), crip.getChave());
        } else if ("descriptografia".equalsIgnoreCase(crip.getOperacao())) {
            return descriptografar(crip.getSenha(), crip.getChave());
        }
        return "Operação inválida. Use 'criptografar' ou 'descriptografar'.";
    }

    public String criptografar(String senha, String chave) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(chave);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(senha.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar a senha", e);
        }
    }

    public String descriptografar(String senha, String chave) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(chave);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedSenha = Base64.getDecoder().decode(senha);
            byte[] decryptedBytes = cipher.doFinal(decodedSenha);
            return new String(decryptedBytes); // Converte para String
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar a senha", e);
        }
    }
}
