# tea-buddy

a notification system that informs you when new stuff is up for sale in your favourite tea store.

## Building

1. Checkout sources
2. Build with `./gradlew build`

## Configuration

The service requires the following environment variables:

- `MAILJET_KEY` - a [Mailjet](https://www.mailjet.com) key that is used for sending notification emails
- `MAILJET_SECRET`- a [Mailjet](https://www.mailjet.com) secret that is used for sending notification emails
- `NOTIFICATION_SENDER` - an email that is configured in Mailjet and is used as a sender of notifications
- `NOTIFICATION_RECIPIENTS` - a comma separated list of notification recipients. Addresses must follow [RFC822](https://www.w3.org/Protocols/rfc822/) syntax.

## Usage

Run `java -jar tea-buddy-X.X.X-SNAPSHOT.jar`

## Docker Image

Run `./gradlew build && docker build -t tea-buddy .`

## License

Copyright © 2024 Pavel Prokopenko
