package hex.gbm;

import hex.gbm.DTree.TreeModel.CompressedTree;
import water.*;
import water.fvec.Chunk;

public abstract class DTreeScorer<T extends DTreeScorer<T>> extends MRTask2<T> {

  /* @IN */ final protected int _ncols;
  /* @IN */ final protected int _nclass;
  /* @IN */ final protected Key[][] _treeKeys;

  protected transient CompressedTree[][] _trees;

  public DTreeScorer(int ncols, int nclass, Key[][] treeKeys) {
    _ncols = ncols;
    _nclass = nclass;
    _treeKeys = treeKeys;
  }

  @Override protected final void setupLocal() {
    int ntrees = _treeKeys.length;
    _trees = new CompressedTree[ntrees][];
    for (int t=0; t<ntrees; t++) {
      Key[] treek = _treeKeys[t];
      _trees[t] = new CompressedTree[treek.length];
      // FIXME remove get by introducing fetch class for all trees
      for (int i=0; i<treek.length; i++) {
        if (treek[i]!=null)
          _trees[t][i] = UKV.get(treek[i]);
      }
    }
  }

  protected final Chunk chk_oobt(Chunk chks[]) { return chks[_ncols+1+_nclass+_nclass+_nclass]; }
  protected final Chunk chk_tree(Chunk chks[], int c) { return chks[_ncols+1+c]; }
  protected final Chunk chk_resp( Chunk chks[] ) { return chks[_ncols]; }

  protected void score0(double data[], float preds[], CompressedTree[] ts) {
    DTreeUtils.scoreTree(data, preds, ts);
  }

}
