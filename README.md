🐴 VetEquina API
================

API desenvolvida com **Spring Boot 17** para o sistema "Área do Cliente", focada na gestão veterinária de equinos, controle de prontuários e acompanhamento de atletas e potros.

🛠️ Tecnologias e Dependências
------------------------------

*   **Framework**: Spring Boot 3.5.6
    
*   **Linguagem**: Java 17
    
*   **Persistência**: Spring Data JPA / Hibernate
    
*   **Banco de Dados**: PostgreSQL (Supabase)
    
*   **Segurança**: Spring Security com OAuth2 Resource Server (JWT via Supabase)
    
*   **Utilitários**: Lombok, MapStruct
    

🚀 Funcionalidades Principais
-----------------------------

*   **Gestão de Perfis**: Sincronização automática de usuários com o Supabase Auth via ProfileSyncFilter.
    
*   **Controle de Equinos**: Cadastro completo de cavalos, incluindo raça, data de nascimento e foto, vinculado ao proprietário.
    
*   **Prontuário Médico**: Registro de atendimentos e observações clínicas associados a cada animal.
    
*   **Segurança RLS (Row Level Security)**: A lógica de serviço garante que usuários (tutores) acessem apenas seus próprios animais e registros.
    
*   **Módulos Específicos**: Estrutura preparada para acompanhamento de crescimento (Potros) e histórico de competições (Atletas).
    

⚙️ Configuração e Execução
--------------------------

### Pré-requisitos

*   Java 17 instalado.
    
*   Maven Wrapper.
    

### Variáveis de Ambiente

As configurações de conexão estão localizadas em src/main/resources/application.properties. Certifique-se de configurar:

*   spring.datasource.url: URL do pooler do Supabase.
    
*   spring.security.oauth2.resourceserver.jwt.jwk-set-uri: Endpoint de chaves do Supabase para validação de tokens.
    

### Comandos


Para rodar o projeto localmente:

```bash
./mvnw spring-boot:run
```

Para executar os testes de integração e unitários:

``` bash
./mvnw test
```

📍 Endpoints Principais (API)
-----------------------------

*   **GET /api/me**
    
    *   Retorna o perfil do usuário logado.
        
*   **GET /api/horses**
    
    *   Lista os cavalos do tutor autenticado.
        
*   **POST /api/horses**
    
    *   Cadastra um novo equino.
        
*   **GET /api/horses/{id}/records**
    
    *   Lista prontuários de um cavalo específico.
        
*   **POST /api/users/sync**
    
    *   Sincroniza metadados do Auth com a tabela de perfis
