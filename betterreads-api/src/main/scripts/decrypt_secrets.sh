#!/bin/bash

gpg --quiet --batch --yes --decrypt --passphrase="$APPLICATION_YML_PASSPHRASE" \
--output src/main/resources/application.sensitive.yml src/main/resources/application.sensitive.yml.gpg