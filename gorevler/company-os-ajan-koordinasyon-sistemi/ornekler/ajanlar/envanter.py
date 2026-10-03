"""Araştırmacı ajan: hedef Python paketinin envanterini çıkarır (dosya, satır, fonksiyon)."""
import ast, json, os, sys

target = sys.argv[1]
inv = {"target": os.path.basename(target.rstrip("/")), "files": []}
for name in sorted(os.listdir(target)):
    if not name.endswith(".py"):
        continue
    src = open(os.path.join(target, name), encoding="utf-8").read()
    tree = ast.parse(src)
    funcs = sorted({n.name for n in ast.walk(tree) if isinstance(n, (ast.FunctionDef, ast.AsyncFunctionDef))})
    calls = sorted({ast.unparse(n.func) for n in ast.walk(tree) if isinstance(n, ast.Call)
                    and ast.unparse(n.func) in ("subprocess.run", "subprocess.Popen", "os.open", "os.replace",
                                               "os.rename", "os.symlink", "open")})
    inv["files"].append({"file": name, "lines": src.count("\n"), "functions": funcs, "sensitive_calls": calls})
json.dump(inv, open("envanter.json", "w", encoding="utf-8"), ensure_ascii=False, indent=1)
print(f"{len(inv['files'])} dosya envantere alındı")
