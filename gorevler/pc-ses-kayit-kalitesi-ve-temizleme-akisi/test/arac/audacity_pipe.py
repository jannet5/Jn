import sys, os
TO='/tmp/audacity_script_pipe.to.0'; FROM='/tmp/audacity_script_pipe.from.0'
_to=open(TO,'w'); _from=open(FROM,'r')
def cmd(c):
    _to.write(c+'\n'); _to.flush()
    out=[]
    while True:
        line=_from.readline()
        if line=='' : break
        out.append(line)
        if line.startswith('BatchCommand finished'):
            _from.readline(); break
    return ''.join(out)
if __name__=='__main__':
    for c in sys.argv[1:]: print(cmd(c))
