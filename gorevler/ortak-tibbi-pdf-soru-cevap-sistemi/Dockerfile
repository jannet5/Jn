# syntax=docker/dockerfile:1
FROM python:3.11-slim
WORKDIR /srv
COPY requirements.txt .
# Kurumsal vekil sunucu arkasında derlerken: docker build --secret id=ca,src=ca.crt .
# (secret verilmezse etkisizdir)
RUN --mount=type=secret,id=ca,required=false \
    if [ -f /run/secrets/ca ]; then export PIP_CERT=/run/secrets/ca; fi; \
    pip install --no-cache-dir -r requirements.txt
ENV MEDPDF_EMBED_CACHE=/srv/models MEDPDF_DATA_DIR=/data
# Modeli imaja göm: ilk istek beklemesin, çalışma anında internet gerekmesin
RUN --mount=type=secret,id=ca,required=false \
    if [ -f /run/secrets/ca ]; then export REQUESTS_CA_BUNDLE=/run/secrets/ca SSL_CERT_FILE=/run/secrets/ca; fi; \
    python -c "from fastembed import TextEmbedding; TextEmbedding('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2', cache_dir='/srv/models')"
COPY app app
COPY static static
VOLUME /data
EXPOSE 8000
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000", "--proxy-headers"]
