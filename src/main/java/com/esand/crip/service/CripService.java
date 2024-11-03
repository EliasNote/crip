package com.esand.crip.service;

import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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

    public Object processar(Object senha, String chave, String operacao) {
        if (senha instanceof List<?>) {
            List<?> lista = (List<?>) senha;

            if (lista.isEmpty() || lista.stream().allMatch(item -> item instanceof String)) {
                return processarLista(chave, operacao, (List<String>) lista);
            } else {
                return "A lista deve conter apenas Strings.";
            }
        } else if (senha instanceof String) {
            if ("criptografia".equalsIgnoreCase(operacao)) {
                return criptografar((String) senha, chave);
            } else if ("descriptografia".equalsIgnoreCase(operacao)) {
                return descriptografar((String) senha, chave);
            }
        }

        return "Operação inválida. Use 'criptografia' ou 'descriptografia'.";
    }

    public String criptografar(String senha, String chave) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(chave);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(senha.getBytes());
            return senha + " -> " + Base64.getEncoder().encodeToString(encryptedBytes);
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
            return senha + " -> " + new String(cipher.doFinal(decodedSenha));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar a senha", e);
        }
    }

    public List<String> processarLista(String chave, String operacao, List<String> senhas) {
        List<String> resultado = new ArrayList<>();

        if ("criptografia".equalsIgnoreCase(operacao)) {
            resultado = senhas.stream().map(x -> criptografar(x, chave)).toList();
        } else if ("descriptografia".equalsIgnoreCase(operacao)) {
            resultado = senhas.stream().map(x -> descriptografar(x, chave)).toList();
        }

        return resultado;
    }
}
