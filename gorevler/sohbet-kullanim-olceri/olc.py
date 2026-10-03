#!/usr/bin/env python3
"""Kurulumsuz giriş noktası: python olc.py <komut> (Windows'ta: py olc.py rapor)."""
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from sohbet_olcer.__main__ import main  # noqa: E402

if __name__ == "__main__":
    sys.exit(main())
