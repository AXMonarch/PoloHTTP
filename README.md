# 🌐 PoloHTTP – A Java HTTP Server

> A hand-crafted HTTP/1.0 server built from scratch in pure Java.

---

## 📦 Overview

**PoloHTTP** is a minimalist HTTP server that parses raw TCP requests, routes paths, builds responses, and serves them to clients like browsers or `curl`. 
It's designed as a learning tool and a demonstration of low-level web server mechanics—layer by layer, protocol-compliant, and written with clarity in mind.
---

## ✨ Features

- 🔌 Built directly on `ServerSocket` and `Socket` (TCP)
- 📜 Fully parses HTTP/1.0 requests (method, path, headers)
- 🚏 Path-based routing 
- 📄 Sends correct status lines, headers, and response bodies
- 🪵 Logging system prints request and response info to console
- 🧪 Testable with `curl`, `telnet`, or your browser
