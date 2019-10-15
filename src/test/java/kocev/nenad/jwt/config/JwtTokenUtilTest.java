package kocev.nenad.jwt.config;


import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTest {

    @Mock
    private JwtSecret jwtSecret;

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp(){
        when(jwtSecret.getSecret()).thenReturn("secret");
        jwtTokenUtil = new JwtTokenUtil(jwtSecret);
    }

    @Test
    public void changedSignatureToken() {
        //given
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("nenad");

        //when
        String token = jwtTokenUtil.generateToken(userDetails).concat("a");

        //then
        assertThrows(SignatureException.class, () -> jwtTokenUtil.validateToken(userDetails, token));
    }

    @Test
    public void validToken() {
        //given
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("nenad");

        //when
        String token = jwtTokenUtil.generateToken(userDetails);
        boolean tokenValidated = jwtTokenUtil.validateToken(userDetails, token);

        //then
        assertThat(tokenValidated).isTrue();
    }

    @Test
    public void invalidToken(){
        //given
        UserDetails authenticatedUser = mock(UserDetails.class);
        given(authenticatedUser.getUsername()).willReturn("nenad");
        UserDetails intruder = mock(UserDetails.class);
        given(intruder.getUsername()).willReturn("n3nad");

        //when
        String token = jwtTokenUtil.generateToken(authenticatedUser);
        boolean tokenValidated = jwtTokenUtil.validateToken(intruder, token);

        //then
        assertThat(tokenValidated).isFalse();
    }
}
