package com.XX_DOMAIN_NAME.XX_APP_NAME.security;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.BusinessLogicException;

@Configuration
public class KeyStoreConfig {
	
	private final Logger log = LoggerFactory.getLogger(KeyStoreConfig.class);
	
	@Value("${app.security.jwt.keystore-location}")
	private String keyStorePath;
	
	@Value("${app.security.jwt.keystore-password}")
	private String keyStorePassw;
	
	@Value("${app.security.jwt.key-alias}")
	private String keyAlias;
	
	@Value("${app.security.jwt.pvt-key-passphrase}")
	private String pvtKeyPassphrase;
	
	@Bean
	public KeyStore keystore() {
		try {
			KeyStore ks=KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
			ks.load(is,keyStorePassw.toCharArray());
			return ks;
		}catch(Exception e) {
			log.error(e.getMessage());
			throw new BusinessLogicException("err.general.internal_server_error", 500);
		}
	}
	
	@Bean
	public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
		try {
			Key k=keyStore.getKey(keyAlias,pvtKeyPassphrase.toCharArray());
			if(k instanceof RSAPrivateKey) return (RSAPrivateKey) k;
			else throw new BusinessLogicException("err.general.internal_server_error", 500);
		}catch(Exception e) {
			log.error(e.getMessage());
			throw new BusinessLogicException("err.general.internal_server_error", 500);
		}
	}
	
	@Bean
	public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
		try {
			Certificate cert=keyStore.getCertificate(keyAlias);
			PublicKey pub=cert.getPublicKey();
			if(pub instanceof RSAPublicKey) return (RSAPublicKey) pub;
			else throw new BusinessLogicException("err.general.internal_server_error", 500);
		}catch(Exception e) {
			log.error(e.getMessage());
			throw new BusinessLogicException("err.general.internal_server_error", 500);
		}
	}

}
