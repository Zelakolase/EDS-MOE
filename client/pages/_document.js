import Document, { Html, Head, Main, NextScript } from "next/document";
import React from "react";
export default class MyDocument extends Document {
	render() {
		return (
			<Html lang='en' dir='ltr'>
				<Head>
					<meta charSet='utf-8' />
					<link rel='preconnect' href='https://fonts.googleapis.com' />
					<link rel='preconnect' href='https://fonts.gstatic.com' />
					<link
						href='https://fonts.googleapis.com/css2?family=Roboto&display=swap'
						rel='stylesheet'
					/>
				</Head>
				<body>
					<Main />
					<NextScript />
				</body>
			</Html>
		);
	}
}
