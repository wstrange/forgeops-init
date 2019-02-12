ARG REPO=forgerock-docker-public.bintray.io/forgerock/ig
ARG TAG=latest
FROM $REPO:$TAG

COPY --chown=forgerock ./ /ig

# IG wants to create files under /ig/{logs,scripts}
# USER root
# RUN chown -R forgerock:forgerock /ig
#USER forgerock

