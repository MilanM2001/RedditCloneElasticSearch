package sr57.ftn.reddit.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(
            AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {

        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean()
            throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter
                .setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.headers().cacheControl().disable();
        httpSecurity.cors();

        httpSecurity.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/posts/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/allElastic").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/single/{post_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/findAllByTitle/{title}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/findAllByText/{text}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/findAllByPDFDescription/{pdfDescription}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/findAllByTwoFields/{first}/{second}/{selectedFields}/{boolQueryType}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/findAllByFlairName/{name}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/findAllByCommentsText/{text}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/karma/{from}/to/{to}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/numberOfComments/{from}/to/{to}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/comments/{post_id}").permitAll()

                .antMatchers(HttpMethod.GET, "/api/communities/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/allElastic").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/single/{community_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/findAllByName/{name}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/findAllByDescription/{description}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/findAllByPDFDescription/{pdfDescription}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/findAllByRulesDescription/{description}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/findAllByTwoFields/{first}/{second}/{selectedFields}/{boolQueryType}/{searchType}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/numberOfPosts/{from}/to/{to}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/averageKarma/{from}/to/{to}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/posts/{community_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/elasticPosts/{name}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/rules/{community_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/communities/flairs/{community_id}").permitAll()

                .antMatchers(HttpMethod.GET, "/api/users/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/single/{user_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/getMe").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/login").permitAll()

                .antMatchers(HttpMethod.GET, "/api/flairs/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/flairs/{flair_id}").permitAll()

                .antMatchers(HttpMethod.GET, "/api/rules/{rule_id}").permitAll()

                .antMatchers(HttpMethod.GET, "/api/comments/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/comments/{comment_id}").permitAll()

                .antMatchers(HttpMethod.GET, "/api/reactions/postKarma/{post_id}").permitAll()

                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}
