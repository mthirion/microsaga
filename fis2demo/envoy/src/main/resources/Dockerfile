FROM lyft/envoy

RUN apt-get update && apt-get -q install -y \
    curl \
    dnsutils
COPY envoy.json /etc/envoy.json
EXPOSE 9998
CMD /usr/local/bin/envoy -c /etc/envoy.json
