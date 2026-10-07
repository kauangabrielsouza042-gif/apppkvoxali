# Voxali Android — GitHub Actions

1. Suba o conteúdo deste ZIP para o repositório.
2. Abra **Actions**.
3. Selecione **Criar APK do Voxali**.
4. Clique em **Executar fluxo de trabalho**.
5. Aguarde terminar.
6. Abra a execução concluída e baixe o artefato **Voxali-APK**.

O workflow instala Gradle 8.13 diretamente, usa Java 17 e gera `Voxali.apk`.

O app tenta sincronizar com `https://voxali.com.br/api/sync` a cada 5 segundos.
O endpoint precisa existir no backend para a sincronização real funcionar.
