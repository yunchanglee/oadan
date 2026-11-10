"""오아단 디자인 시안 HTML → PNG 렌더링 스크립트"""
import sys
from pathlib import Path
from playwright.sync_api import sync_playwright

ROOT = Path("/home/andrew/git/oadan/design")
HTML = ROOT / "oadan_mockup.html"
OUT = ROOT / "export"
OUT.mkdir(exist_ok=True)

with sync_playwright() as p:
    b = p.chromium.launch(executable_path=str(Path.home() / ".cache/ms-playwright/chromium-1254/chrome-linux64/chrome"))
    pg = b.new_page(viewport={"width": 1640, "height": 1000}, device_scale_factor=2)
    pg.goto(HTML.as_uri(), wait_until="networkidle")
    pg.evaluate("document.fonts.ready")
    pg.wait_for_timeout(800)
    pg.screenshot(path=str(OUT / "00_overview.png"), full_page=True)

    pg.locator(".board").screenshot(path=str(OUT / "01_foundations.png"))
    for i, shot in enumerate(pg.locator(".shot").all()):
        sid = shot.locator(".cap b").inner_text().strip()
        shot.locator(".phone").screenshot(path=str(OUT / f"{sid}.png"))
    pg.locator(".widget-board").screenshot(path=str(OUT / "05_widgets.png"))
    b.close()
print("ok", sorted(x.name for x in OUT.iterdir()))
