FROM tsl0922/ttyd:latest as ttyd

FROM ubuntu:24.04 as tofu

RUN apt-get update  \
 && apt-get install -y --no-install-recommends ca-certificates curl gpg unzip

RUN curl -fsSL https://get.opentofu.org/install-opentofu.sh -o install-opentofu.sh  \
 && chmod +x install-opentofu.sh

RUN ./install-opentofu.sh --install-method standalone --install-path / --symlink-path -

FROM ubuntu:24.04

RUN apt-get update  \
 && apt-get install -y --no-install-recommends  \
      ca-certificates \
      gpg \
      gpg-agent \
      curl \
      tini \
 && rm -rf /var/lib/apt/lists/*

COPY --from=tofu /tofu /usr/local/bin/
COPY --from=ttyd /usr/bin/ttyd /usr/bin/

EXPOSE 2222
WORKDIR /root

ENTRYPOINT ["/usr/bin/tini", "--"]
CMD [ "ttyd", \
  "-W", \
  "-p", "2222", \
  "--terminal-type=xterm-256color",  \
  "-t", "titleFixed=TTYD",  \
  "-t", "cursorStyle=bar",  \
  "-t", "cursorBlink=true",  \
  "-t", "cursorInactiveStyle=underline", \
  "-t", "drawBoldTextInBrightColors=true", \
  "-t", "cursorWidth=4", \
  "-t", "lineHeight=1", \
  "-t", "theme={\"background\": \"black\",\"foreground\":\"green\"}", \
  "-t", "fontSize=22", \
  "-t", "scrollOnUserInput=true", \
  "-t", "convertEol=true", \
  "-t", "max-clients=1", \
  "bash" \
]