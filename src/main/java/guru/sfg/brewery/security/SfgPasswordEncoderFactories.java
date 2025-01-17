package guru.sfg.brewery.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class SfgPasswordEncoderFactories {
  public static PasswordEncoder createDelegatingPasswordEncoder() {
    String encodingId = "bcrypt10";
    Map<String, PasswordEncoder> encoders = new HashMap();
    encoders.put(encodingId, new BCryptPasswordEncoder(10));
    encoders.put("bcrypt", new BCryptPasswordEncoder());
    encoders.put("ldap", new LdapShaPasswordEncoder());
    encoders.put("noop", NoOpPasswordEncoder.getInstance());
    encoders.put("sha256", new StandardPasswordEncoder());
    return new DelegatingPasswordEncoder(encodingId, encoders);
  }

  // don't initialize class
  private SfgPasswordEncoderFactories() {
  }
}