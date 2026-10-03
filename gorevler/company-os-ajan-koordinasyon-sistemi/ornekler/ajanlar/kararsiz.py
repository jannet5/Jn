"""İlk denemede çöken (ör. worker ölümü) ikinci denemede başaran ajan: yeniden deneme kanıtı."""
import os, sys
if os.environ["COS_ATTEMPT"] == "1":
    print("simüle edilmiş çökme", file=sys.stderr)
    sys.exit(2)
open("kararli.txt", "w").write(f"deneme {os.environ['COS_ATTEMPT']} başarılı\n")
print("ikinci denemede tamam")
