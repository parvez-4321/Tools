# Tools

A lightweight, responsive collection of browser-based utilities. Built with plain HTML, CSS, and JavaScript, so it needs no build step or backend.

## Included tools

- Word and character counter
- Uppercase, lowercase, title case, and sentence case converter
- JSON formatter, minifier, and validator
- URL encoder and decoder
- Base64 encoder and decoder
- Random password generator

## Run locally

Open `index.html` in a modern browser. For local hosting, you can also run:

```bash
python -m http.server 8000
```

Then visit `http://localhost:8000`.

## Deploy with GitHub Pages

1. Open the repository's **Settings**.
2. Select **Pages** in the sidebar.
3. Under **Build and deployment**, choose **Deploy from a branch**.
4. Select branch `main` and folder `/ (root)`, then save.
5. Wait for GitHub Pages to publish the site. The public URL will appear in the Pages settings.

## Privacy

All tools process inputs in the browser. This project does not include an API or send user-entered data to a server. Use a modern browser for secure random password generation and clipboard access.

## License

MIT
