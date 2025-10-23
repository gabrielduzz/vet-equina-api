package br.com.vetequina.api.security;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import br.com.vetequina.api.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileSyncFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        // pule sync em rotas públicas/health/doc
        return p.startsWith("/actuator")
                || p.startsWith("/swagger-ui")
                || p.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();

            String sub = jwt.getClaimAsString("sub");
            if (sub != null && !sub.isBlank()) {
                try {
                    UUID userId = UUID.fromString(sub);
                    String email = jwt.getClaimAsString("email");

                    // tentar nomes de user_metadata (se existirem)
                    String firstName = null, lastName = null;
                    Object metaObj = jwt.getClaim("user_metadata");
                    if (metaObj instanceof Map<?, ?> meta) {
                        Object fn = meta.get("first_name");
                        Object ln = meta.get("last_name");
                        if (fn instanceof String s)
                            firstName = s;
                        if (ln instanceof String s)
                            lastName = s;
                        // Se tiver "name" único, você pode dividir em first/last aqui.
                    }

                    userService.upsertFromAuth(userId, email, firstName, lastName);
                } catch (Exception e) {
                    // Não quebre a request por falha de sync; logue se quiser
                    // e siga o fluxo (ou troque por 500, sua decisão de produto)
                }
            }
        }

        chain.doFilter(request, response);
    }
}
