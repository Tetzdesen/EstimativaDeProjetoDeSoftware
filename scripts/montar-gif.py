#!/usr/bin/env python3
"""Monta docs/imagens/recalculo-ao-vivo.gif a partir dos quadros do GeradorVitrine.

Uso: python3 scripts/montar-gif.py [pasta-dos-quadros] [arquivo-gif]
"""
import sys
from pathlib import Path

from PIL import Image

quadros = Path(sys.argv[1] if len(sys.argv) > 1 else "target/vitrine/quadros")
saida = Path(sys.argv[2] if len(sys.argv) > 2 else "docs/imagens/recalculo-ao-vivo.gif")
ESCALA = 0.75
LIMITE = 2 * 1024 * 1024

arquivos = sorted(quadros.glob("quadro-*.png"))
if not arquivos:
    sys.exit(f"nenhum quadro em {quadros}")
imagens = []
for arq in arquivos:
    img = Image.open(arq).convert("RGB")
    img = img.resize((int(img.width * ESCALA), int(img.height * ESCALA)), Image.LANCZOS)
    imagens.append(img.quantize(colors=256, method=Image.Quantize.FASTOCTREE, dither=Image.Dither.NONE))
duracoes = [1300] * len(imagens)
duracoes[0] = 1800
duracoes[-1] = 3500
saida.parent.mkdir(parents=True, exist_ok=True)
imagens[0].save(saida, save_all=True, append_images=imagens[1:], duration=duracoes, loop=0, optimize=True)
tamanho = saida.stat().st_size
print(f"{saida}: {len(imagens)} quadros, {tamanho / 1024:.0f} KB")
if tamanho > LIMITE:
    sys.exit("GIF maior que 2 MB")
